# python-backend/db_connect/rds_connector.py

import mysql.connector
from mysql.connector import Error

def connect_rds(host, user, password, database):
    try:
        conn = mysql.connector.connect(
            host=host,
            user=user,
            password=password,
            database=database
        )
        if conn.is_connected():
            print("✅ Connected to Amazon RDS MySQL")
            return conn
    except Error as e:
        print(f"❌ RDS Connection Error: {e}")
        return None

def fetch_all_records(conn):
    try:
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM users;")
        records = cursor.fetchall()
        print(f"📦 Total records fetched: {len(records)}")
        for row in records:
            print(row)
        return records
    except Exception as e:
        print(f"❌ Error fetching records: {e}")
        return []

# Run block
if __name__ == "__main__":
    # Replace with actual RDS credentials
    rds_host = "aiawsrdsdynamodb.cnquiqqoi741.ap-south-1.rds.amazonaws.com"
    rds_user = "root"
    rds_password = "Anurag#123"
    rds_database = "aiawsrdsdynamodb"

    conn = connect_rds(rds_host, rds_user, rds_password, rds_database)
    if conn:
        fetch_all_records(conn)
        conn.close()
