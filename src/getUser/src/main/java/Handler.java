import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.Map;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent message, Context context) {
        System.out.println("Message: " + message);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client);

        String id = message.getPathParameters().get("id");
        System.out.println("id: " + id);

        String tableName = System.getenv("TABLE_NAME");
        Table table = dynamoDB.getTable(tableName);

        Item userItem = table.getItem("id", id);

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        if (userItem == null) {
            response.setStatusCode(404);
            return response;
        }
        response.setStatusCode(200);
        response.setBody(userItem.toJSON());

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        return response;
    }
}
