package com.ai.aws.rds.dynamodb.token;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.GetSessionTokenRequest;
import software.amazon.awssdk.services.sts.model.GetSessionTokenResponse;

public class TokenManager {

	private static AwsSessionCredentials sessionCredentials;

	public static AwsSessionCredentials getSessionCredentials() {
		if (sessionCredentials == null) {
			refreshSession();
		}
		return sessionCredentials;
	}

	public static void refreshSession() {
		try {
			Dotenv dotenv = Dotenv.load();
			String accessKey = dotenv.get("AWS_ACCESS_KEY_ID");
			String secretKey = dotenv.get("AWS_SECRET_ACCESS_KEY");
			String region = dotenv.get("AWS_REGION");

			StsClient stsClient = StsClient.builder().region(Region.of(region))
					.credentialsProvider(StaticCredentialsProvider.create(
							software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create(accessKey, secretKey)))
					.build();

			GetSessionTokenResponse response = stsClient
					.getSessionToken(GetSessionTokenRequest.builder().durationSeconds(3600).build());

			sessionCredentials = AwsSessionCredentials.create(response.credentials().accessKeyId(),
					response.credentials().secretAccessKey(), response.credentials().sessionToken());

			System.out.println("✅ STS: Temporary session credentials refreshed.");

		} catch (Exception e) {
			System.err.println("❌ STS Exception: " + e.getMessage());
		}
	}
}
