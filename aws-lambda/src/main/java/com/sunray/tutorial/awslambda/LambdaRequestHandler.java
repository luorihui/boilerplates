package com.sunray.tutorial.awslambda;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaRequestHandler implements RequestHandler<Map<String, Object>, String> {

	@Override
	public String handleRequest(Map<String, Object> input, Context context) {
		LambdaLogger logger = context.getLogger();
        logger.log("Input: " + input);
        return "Hello World - " + input;
	}

}
