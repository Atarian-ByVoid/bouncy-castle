package saxxes.bouncy.demo;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class SSLConfig {

    @Bean
    public SSLContext sslContext() {
        return carregarCertificado();
    }

    private SSLContext carregarCertificado() {
        try {
            // Especifica o caminho do arquivo do certificado no classpath
            Resource resource = new ClassPathResource("certificates/**");
            System.out.println("Deu certo essa poha");
            // Obtém o arquivo a partir do recurso
            File pfx = resource.getFile();
            String pfxpassword = "F7SXS6";
            
            // Carrega o certificado
            InputStream keyInput = new FileInputStream(pfx);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(keyInput, pfxpassword.toCharArray());
            keyInput.close();
            
            // Configura o KeyManagerFactory
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, pfxpassword.toCharArray());
            
            // Configura o SSLContext
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
            
            return context;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar certificado.", e);
        }
    }
}
