import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;


// Handler for DELETE
public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent message, Context context) {
        System.out.println("Message: " + message);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client);

        String id = message.getPathParameters().get("id");
        System.out.println("id: " + id);

        String tableName = System.getenv("TABLE_NAME");

        Table table = dynamoDB.getTable(tableName);

        DeleteItemOutcome outcome = table.deleteItem("id", id);
        System.out.println("Outcome: " + outcome);

        APIGatewayProxyResponseEvent result = new APIGatewayProxyResponseEvent();
        result.setStatusCode(204);
        return result;
    }
}
