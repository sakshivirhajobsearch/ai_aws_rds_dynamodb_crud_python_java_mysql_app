# python-backend/db_connect/dynamodb_connector.py

import boto3
from botocore.exceptions import NoCredentialsError, ClientError

def fetch_dynamodb_items():
    try:
        dynamodb = boto3.resource('dynamodb', region_name='ap-south-1')
        table = dynamodb.Table('users')  # Change to your table name

        response = table.scan()
        items = response.get('Items', [])
        print(f"📦 Total DynamoDB items fetched: {len(items)}")
        for item in items:
            print(item)
        return items

    except NoCredentialsError:
        print("❌ AWS credentials not found.")
    except ClientError as e:
        print(f"❌ DynamoDB Client Error: {e}")
    except Exception as e:
        print(f"❌ Unknown Error: {e}")

# Run block
if __name__ == "__main__":
    fetch_dynamodb_items()
