(ns recepta-regis-fse.services.xades
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

(def cert-pwd-arr
  (.toCharArray (str (:cert-pwd conf/configuration))))

(def ^Pkcs12SignatureToken signing-token
  (let [pwd-protection (KeyStore$PasswordProtection. cert-pwd-arr)]
    (Pkcs12SignatureToken. (File. (:cert-path conf/configuration)) pwd-protection)))

(def ^DSSPrivateKeyEntry private-key
  (.. signing-token getKeys (get 0)))

(def ^XAdESSignatureParameters xades-params
  (let [par (XAdESSignatureParameters.)]
    (doto par
      (.setSignatureLevel SignatureLevel/XAdES_BASELINE_B)
      (.setSignaturePackaging SignaturePackaging/ENVELOPED)
      (.setDigestAlgorithm DigestAlgorithm/SHA256)
      (.setSigningCertificate (.getCertificate private-key))
      (.setCertificateChain (.getCertificateChain private-key))
      (.setXPathLocationString "//*[local-name() = 'signatureCode']")
      (.setXPathElementPlacement  XAdESSignatureParameters$XPathElementPlacement/XPathAfter))))

(def not-nil? (complement nil?))

(defn sign [s]
  {:pre [(not-nil? s)]}
  (let [^DSSDocument doc-to-sign (string->dssdoc s)
        ;;^DSSDocument doc-to-sign (file->dssdoc s)
        ^XAdESSignatureParameters sign-params xades-params
        ^XAdESService sign-service (XAdESService. (CommonCertificateVerifier.))
        ^ToBeSigned data-to-sign (. sign-service getDataToSign doc-to-sign sign-params)
        ^SignatureValue signature-value (. signing-token sign data-to-sign (.getDigestAlgorithm xades-params) private-key)
        ^DSSDocument doc-signed (. sign-service signDocument doc-to-sign sign-params signature-value)]
    (dssdoc->string doc-signed)))
