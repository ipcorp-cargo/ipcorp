package kz.ipcorp.model.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

@Component
public class MobizonSMSSender {
    private static final String apiKey = "kz380e1cac45a01fc13f3222dd362113bf0d1311b1104bacadb39794da2432f4dae70f";
    private static final String verificationMessage = """
            You verification code is %s ,\s
            please don't show anyone else.
            """;
    private static final String baseUrl = "https://api.mobizon.kz/service/message/sendsmsmessage";
    public void sendSMS(String phoneNumber, String verificationCode){
        phoneNumber = phoneNumber.replace("+","").replace(" ","");
        String message = String.format(verificationMessage, verificationCode);

        try {
            HttpClient httpClient = HttpClients.createDefault();

            URIBuilder uriBuilder = new URIBuilder(baseUrl);
            uriBuilder.setParameter("recipient",phoneNumber);
            uriBuilder.setParameter("text",message);
            uriBuilder.setParameter("apiKey",apiKey);

            HttpGet httpGet = new HttpGet(uriBuilder.build());

            HttpResponse httpResponse = httpClient.execute(httpGet);

            int statusCode = httpResponse.getStatusLine().getStatusCode();

            System.out.println("Response code: " + statusCode);

            String responseContent = EntityUtils.toString(httpResponse.getEntity());
            System.out.println("Response content: " + responseContent);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
