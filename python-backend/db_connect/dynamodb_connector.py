# python-backend/db_connect/dynamodb_connector.py
import boto3
from botocore.exceptions import ClientError, NoCredentialsError

# ✅ AWS configuration
AWS_REGION = ''                     # replace with your table's region
AWS_ACCESS_KEY = ''      # replace with your AWS Access Key
AWS_SECRET_KEY = ''  # replace with your AWS Secret Key
TABLE_NAME = ''               # replace with your DynamoDB table name

try:
    # Initialize DynamoDB resource
    dynamodb = boto3.resource(
        'dynamodb',
        region_name=AWS_REGION,
        aws_access_key_id=AWS_ACCESS_KEY,
        aws_secret_access_key=AWS_SECRET_KEY
    )

    # List all tables to verify connection
    client = boto3.client(
        'dynamodb',
        region_name=AWS_REGION,
        aws_access_key_id=AWS_ACCESS_KEY,
        aws_secret_access_key=AWS_SECRET_KEY
    )
    tables = client.list_tables().get('TableNames', [])
    if TABLE_NAME not in tables:
        print(f"❌ Table '{TABLE_NAME}' not found in region {AWS_REGION}.")
        print("Available tables:", tables)
    else:
        # Access table
        table = dynamodb.Table(TABLE_NAME)

        # Scan items
        response = table.scan()
        items = response.get('Items', [])
        print(f"📦 Total items fetched: {len(items)}")
        for item in items:
            print(item)

except NoCredentialsError:
    print("❌ AWS credentials not found. Please set your AWS_ACCESS_KEY and AWS_SECRET_KEY.")
except ClientError as e:
    print(f"❌ DynamoDB Client Error: {e}")
except Exception as e:
    print(f"❌ Unexpected error: {e}")
