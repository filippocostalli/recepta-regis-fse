(ns recepta-regis-fse.services.sign
  (:require
    [recepta-regis-fse.configuration :as conf])
  (:import
    (javax.xml.parsers DocumentBuilderFactory)
    (javax.xml.transform TransformerFactory)
    (javax.xml.transform.stream StreamResult)
    (java.io StringReader)
    (org.xml.sax InputSource)
    (org.w3c.dom Element)
    (xades4j.providers.impl FileSystemKeyStoreKeyingDataProvider)
    (xades4j.providers.impl FirstCertificateSelector)
    (xades4j.providers.impl DirectPasswordProvider)
    (xades4j.production XadesBesSigningProfile)
    (xades4j.production DataObjectReference)))

(defn string->xmldoc [s]
  (let [doc-builder (.. DocumentBuilderFactory newInstance newDocumentBuilder)]
    (.parse doc-builder (InputSource. (StringReader. s)))))


(defn file->xmldoc [path]
  (let [doc-builder (.. DocumentBuilderFactory newInstance newDocumentBuilder)]
    (.parse doc-builder (java.io.File. path))))

(def cert-pwd  (:pwd conf/configuration))

(defn get-kdp []
  (FileSystemKeyStoreKeyingDataProvider.
    "pkcs12"
    (:cert conf/configuration)
    (FirstCertificateSelector.)
    (DirectPasswordProvider. cert-pwd)
    (DirectPasswordProvider. cert-pwd)
    true))

(defn sign-document
  [src dst]
  (let [doc (file->xmldoc src)
        elem (.getDocumentElement doc)
        refer (str "#" (.getAttribute elem "Id"))
        obj (.. (DataObjectReference. refer) withTransform (.EnvelopedSignatureTransform))
        kdp (get-kdp)
        signer (.. (XadesBesSigningProfile. kdp) newSigner)
        transformer (.. TransformerFactory newInstance newTransformer)
        source (.DOMSource doc)
        result (StreamResult. (java.io.File. dst))]
    (.transform transformer source result)))
