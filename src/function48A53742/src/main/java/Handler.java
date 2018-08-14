import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class User {
    // The DB primary key is "id", a string.
    String id;
    String FirstName;
    String LastName;
    String FavoriteColor;
}

// Handler for 'list'
public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent message, Context context) {
        System.out.println("Input message: " + message);
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        Gson gson = new Gson();

        String tableName = System.getenv("TABLE_NAME");
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(tableName);

        ItemCollection<?> col = table.scan();
        List<User> users = new ArrayList<>();
        for (Item item: col) {
             User user = gson.fromJson(item.toJSON(), User.class);
             users.add(user);
        }

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setBody(gson.toJson(users));
        return response;
    }
}
