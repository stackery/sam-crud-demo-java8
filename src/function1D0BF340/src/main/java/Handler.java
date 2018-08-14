import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

class InputData {
    // The DB primary key is "id", a string.
    public String id;
    public String firstName;
    public String lastName;
    public String color;
}

// Handler for POST
public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent message, Context context) {
        System.out.println("Input message: " + message);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDB dynamoDB = new DynamoDB(client);
        Gson gson = new Gson();

        String tableName = System.getenv("TABLE_NAME");
        Table table = dynamoDB.getTable(tableName);

        String body = message.getBody();
        InputData inputData = gson.fromJson(body, InputData.class);

        Item item = new Item()
                .withPrimaryKey("id", inputData.id)
                .withString("FirstName", inputData.firstName)
                .withString("LastName", inputData.lastName)
                .withString("FavoriteColor", inputData.color);

        PutItemOutcome outcome = table.putItem(item);

        return new APIGatewayProxyResponseEvent().withStatusCode(204);
    }
}
