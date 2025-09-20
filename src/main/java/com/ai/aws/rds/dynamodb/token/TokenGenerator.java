package com.ai.aws.rds.dynamodb.token;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.Credentials;
import software.amazon.awssdk.services.sts.model.GetSessionTokenRequest;
import software.amazon.awssdk.services.sts.model.GetSessionTokenResponse;
import software.amazon.awssdk.services.sts.model.StsException;

public class TokenGenerator {

	// ✅ Replace these with valid permanent IAM credentials (NOT temporary)
	private static final String ACCESS_KEY = "";
	private static final String SECRET_KEY = "";

	public static void main(String[] args) {
		try {
			// ✅ Create STS client using basic credentials
			StsClient stsClient = StsClient.builder().region(Region.AP_SOUTH_1) // Replace with your AWS region
					.credentialsProvider(
							StaticCredentialsProvider.create(AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
					.build();

			// ✅ Build session token request (12 hours = 43200 seconds)
			GetSessionTokenRequest tokenRequest = GetSessionTokenRequest.builder().durationSeconds(43200).build();

			// ✅ Call STS to get temporary credentials
			GetSessionTokenResponse tokenResponse = stsClient.getSessionToken(tokenRequest);
			Credentials sessionCreds = tokenResponse.credentials();

			// ✅ Print temporary credentials
			System.out.println("✅ Temporary AWS Security Credentials:");
			System.out.println("Access Key ID:     " + sessionCreds.accessKeyId());
			System.out.println("Secret Access Key: " + sessionCreds.secretAccessKey());
			System.out.println("Session Token:     " + sessionCreds.sessionToken());
			System.out.println("Expires At:        " + sessionCreds.expiration());

		} catch (StsException e) {
			System.err.println("❌ STS Exception: " + e.awsErrorDetails().errorMessage());
			System.err.println("🔁 Reason: Invalid credentials or no permission for sts:GetSessionToken");
		}
	}
}
