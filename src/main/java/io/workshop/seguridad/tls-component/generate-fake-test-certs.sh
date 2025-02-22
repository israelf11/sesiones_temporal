# This scripts generates test keys and certificates for the sample.
# In a production environment such artifacts should be genrated
# by a proper certificate authority and handled in a secure manner.

CERTS_DIR=./certs-fake
mkdir $CERTS_DIR

# Generate a private key and a certificate for a test certificate authority
openssl genrsa -out $CERTS_DIR/ca.key 4096
openssl req -new -x509 -key $CERTS_DIR/ca.key -sha256 -subj "/C=US/ST=WA/O=Fake CA, Inc." -days 365 -out $CERTS_DIR/ca.cert

# Generate a private key and a certificate for cluster 
openssl genrsa -out $CERTS_DIR/cluster.key 4096
openssl req -new -key $CERTS_DIR/cluster.key -out $CERTS_DIR/cluster.csr -config cluster-cert.conf
openssl x509 -req -in $CERTS_DIR/cluster.csr -CA $CERTS_DIR/ca.cert -CAkey $CERTS_DIR/ca.key -CAcreateserial -out $CERTS_DIR/cluster.pem -days 365 -sha256 -extfile cluster-cert.conf -extensions req_ext

# Generate a private key and a certificate for clients
openssl req -newkey rsa:4096 -nodes -keyout "$CERTS_DIR/client.key" -out "$CERTS_DIR/client.csr" -config client-fake-cert.conf
openssl x509 -req -in $CERTS_DIR/client.csr -CA $CERTS_DIR/ca.cert -CAkey $CERTS_DIR/ca.key -CAcreateserial -out $CERTS_DIR/client.pem -days 365 -sha256 -extfile client-fake-cert.conf -extensions req_ext
# Export to .pfx 
# "-keypbe NONE -certpbe NONE -passout pass:" specifies an unencrypted archive
openssl pkcs12 -export -out $CERTS_DIR/client.pfx -inkey $CERTS_DIR/client.key -in $CERTS_DIR/client.pem -keypbe NONE -certpbe NONE -passout pass:
