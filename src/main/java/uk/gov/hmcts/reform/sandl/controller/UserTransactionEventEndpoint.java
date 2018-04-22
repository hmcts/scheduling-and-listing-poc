package uk.gov.hmcts.reform.sandl.controller;

public interface UserTransactionEventEndpoint
{
	public UserTransactionEvent handle(UserTransactionEvent event);
}
