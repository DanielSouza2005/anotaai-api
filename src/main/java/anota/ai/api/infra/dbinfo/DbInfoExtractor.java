package anota.ai.api.infra.dbinfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class DbInfoExtractor {

    private String host;
    private String port;
    private String dbName;

    public DbInfoExtractor(@Value("${spring.datasource.url}") String dbUrl) throws URISyntaxException {
        // remover prefixo "jdbc:" para usar URI
        String url = dbUrl.replace("jdbc:", "");

        URI uri = new URI(url.substring(0, url.indexOf("?") != -1 ? url.indexOf("?") : url.length()));
        this.host = uri.getHost();
        this.port = String.valueOf(uri.getPort() != -1 ? uri.getPort() : 5432);
        this.dbName = uri.getPath().substring(1); // remove a barra inicial
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }
}
