private Dialog autoReplyToBroker(GeneraliPrincipal principal, Contact sourceContact, Contact targetContact, String relatedEntityType, String relatedEntityCode, String dialogType, String message) {
	try {
		Dialog dialog = null;
		if (DialogTags.TASK_ASSISTANCE_ANNOUNCEMENT.getCode().equals(dialogType)) {
			QnaMakerKnowledgeBaseRestClient client = new QnaMakerKnowledgeBaseRestClient();
			String jsonString = "{\"question\":\"" + message + "\"}";
			JSONObject restInput = new JSONObject(jsonString);
			JSONObject restOutput = client.getPrimarysKnowledgeBaseAnswers(restInput);
			JSONArray jsArray = ((JSONArray) restOutput.getJSONArray("answers"));
			Double finalScore = 0.00;
			String finalAnswer = "";
			if (jsArray != null) {
				if (jsArray.length() > 0) {
					for (int i = 0; i < jsArray.length(); i++) {
						JSONObject jsObject = (JSONObject) jsArray.get(i);
						String answer = (String) jsObject.get("answer");
						Double score = (Double) jsObject.get("score");
						if (score > finalScore) {
							if (answer != null) {
								answer = Converters.asTrimmedString(answer);
								finalScore = score;
								finalAnswer = answer;
							}
						}
					}
				}
			}
			if (finalAnswer.length() > 0) {
				dialog = notifyBrokerForComment(principal, sourceContact, targetContact, relatedEntityType, relatedEntityCode, dialogType, finalAnswer);
			}
		}

		return dialog;
	} catch (Exception e) {
		throw new DataAccessException("Error while running taskService.autoReplyToBroker", e);
	}
}
