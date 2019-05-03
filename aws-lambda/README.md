#aws-lambda
Boilerplate project to create cron job in AWS lambda using Spring Boot 2. The generated zip needs to be uploaded to AWS lambda console and entry function specified as well.

#Notes
1. By default Spring boot 2 generate a fat jar that put the Java class files under BOOT-INF which don't recognized by AWS classloader. Hence creating a zip format and put the class files under root instead.
1. LambdaRequestHandler::handleRequest is the entry point and needs to be specified in AWS console. The input type is a LinkedHashMap.
1. Do keep in mind that lambda function can run at most 15min before it times out. AWS lambda would terminate the function is it exceeds the time. For long running tasks, consider break it up to smaller jobs or using Amazon Batch.

#Build

	mvn clean package

The zip file is located in the target folder.
