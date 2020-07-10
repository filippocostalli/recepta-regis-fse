(ns recepta-regis-fse.services.xades-mul
  (:require
    [recepta-regis-fse.configuration :as conf])
  (:import
    (java.lang String)
    (java.io File ByteArrayOutputStream)
    (java.security KeyStore$PasswordProtection)
    (eu.europa.esig.dss.model DSSDocument InMemoryDocument ToBeSigned SignatureValue FileDocument)
    (eu.europa.esig.dss.enumerations DigestAlgorithm SignatureLevel SignaturePackaging)
    (eu.europa.esig.dss.validation CommonCertificateVerifier)
    (eu.europa.esig.dss.token Pkcs12SignatureToken DSSPrivateKeyEntry)
    (eu.europa.esig.dss.xades XAdESSignatureParameters)
    (eu.europa.esig.dss.xades XAdESSignatureParameters$XPathElementPlacement)
    (eu.europa.esig.dss.xades.signature XAdESService)))

(defn ^InMemoryDocument string->dssdoc [s]
  (InMemoryDocument. (.getBytes s "UTF-8")))

(defn ^FileDocument file->dssdoc [s]
    (FileDocument. s))

(defn dssdoc->string
  [^DSSDocument doc]
  (let [stream (ByteArrayOutputStream.)]
    (doto doc
      (.writeTo stream))
    (String. (.toByteArray stream))))

(defn cert-pwd-arr [presidio-id]
   (let [presidio-kword (keyword (str presidio-id))]
     (-> conf/configuration
         (get-in [:certificates presidio-kword :cert-pwd])
         str
         .toCharArray)))

(defn ^File cert-file [presidio-id]
   (let [presidio-kword (keyword (str presidio-id))]
     (-> conf/configuration
         (get-in [:certificates presidio-kword :cert-path])
         File.)))

(defn ^Pkcs12SignatureToken signing-token [presidio-id]
    (Pkcs12SignatureToken.
      (cert-file presidio-id)
      (KeyStore$PasswordProtection. (cert-pwd-arr presidio-id))))

(defn ^XAdESSignatureParameters xades-params [presidio-id]
  (let [par (XAdESSignatureParameters.)]
    (doto par
      (.setSignatureLevel SignatureLevel/XAdES_BASELINE_B)
      (.setSignaturePackaging SignaturePackaging/ENVELOPED)
      (.setDigestAlgorithm DigestAlgorithm/SHA256)
      (.setSigningCertificate (.getCertificate (private-key presidio-id)))
      (.setCertificateChain (.getCertificateChain (private-key presidio-id)))
      (.setXPathLocationString "//*[local-name() = 'signatureCode']")
      (.setXPathElementPlacement  XAdESSignatureParameters$XPathElementPlacement/XPathAfter))))

(def not-nil? (complement nil?))

(defn sign [s presidio-id]
  {:pre [(not-nil? s)]}
  (let [^DSSDocument doc-to-sign (string->dssdoc s)
        ;;^DSSDocument doc-to-sign (file->dssdoc s)
        ^Pkcs12SignatureToken sign-token (signing-token presidio-id)
        ^DSSPrivateKeyEntry private-key   (.. sign-token getKeys (get 0))
        ^XAdESSignatureParameters sign-params (xades-params presidio-id)
        ^XAdESService sign-service (XAdESService. (CommonCertificateVerifier.))
        ^ToBeSigned data-to-sign (. sign-service getDataToSign doc-to-sign sign-params)
        ^SignatureValue signature-value (. sign-token sign data-to-sign (.getDigestAlgorithm sign-params) private-key)
        ^DSSDocument doc-signed (. sign-service signDocument doc-to-sign sign-params signature-value)]
    (dssdoc->string doc-signed)))
