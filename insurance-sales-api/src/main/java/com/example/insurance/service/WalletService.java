package com.example.insurance.service;

import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.gateway.Identities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.nio.file.Files;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Value("${credentials.path}")
    private String credentialsPath;

    public void addAdminIdentity(Wallet wallet) throws Exception {
        logger.info("Adding admin identity with credentialsPath: {}", credentialsPath);

        if (credentialsPath == null) {
            logger.error("credentialsPath is null");
            throw new IllegalArgumentException("credentialsPath is null");
        }

        Path credentialPath = Paths.get(credentialsPath);
        Path certificatePath = credentialPath.resolve("admin-cert.pem");
        Path privateKeyPath = credentialPath.resolve("admin-key.pem");

        X509Certificate certificate = readX509Certificate(certificatePath);
        PrivateKey privateKey = getPrivateKey(privateKeyPath);

        X509Identity identity = Identities.newX509Identity("Org1MSP", certificate, privateKey);
        wallet.put("admin", identity);
    }

    private X509Certificate readX509Certificate(Path certificatePath) throws IOException, CertificateException {
        try (InputStream in = Files.newInputStream(certificatePath)) {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(in);
        }
    }

    private PrivateKey getPrivateKey(Path privateKeyPath) throws IOException {
        try (InputStream in = Files.newInputStream(privateKeyPath);
                PEMParser pemParser = new PEMParser(new InputStreamReader(in))) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            if (object instanceof PEMKeyPair) {
                return converter.getKeyPair((PEMKeyPair) object).getPrivate();
            } else if (object instanceof PrivateKeyInfo) {
                return converter.getPrivateKey((PrivateKeyInfo) object);
            } else {
                throw new IOException("Invalid private key format");
            }
        }
    }
}
