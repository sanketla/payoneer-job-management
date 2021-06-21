package com.payoneer.jobmanagement.delayjobs;

import com.payoneer.jobmanagement.enums.JobStatus;
import com.payoneer.jobmanagement.enums.JobType;
import com.payoneer.jobmanagement.exceptions.SendEmailJobException;

public class SendEmailDelayJob extends DelayJob {

	public SendEmailDelayJob(long delayTime) {
		super(JobType.SEND_EMAIL, delayTime);
	}

	@Override
	public void run() {
		try {
			System.out.println(String.format("(ThreadId:JobId:JobType:DelayTime)-->(%s:%s:%s:%s)", Thread.currentThread().getId(), getJobId(), getJobType(), getDelayTime()));
			setJobStatus(JobStatus.RUNNING);
			performJobActions();
			setJobStatus(JobStatus.SUCCESS); 
		} catch (SendEmailJobException e) {
			//exception needs special handling.
			setJobStatus(JobStatus.FAILED);
			setErrorMessage(e.getLocalizedMessage());
			System.out.println(String.format("(ThreadId:JobId:JobType:DelayTime)-->(%s:%s:%s:%s)-->Failed job. Reason: %s", Thread.currentThread().getId(), getJobId(), getJobType(), getDelayTime(), e.getLocalizedMessage()));
		}
	}

	private void performJobActions() throws SendEmailJobException {
		//Perform all the job actions in the single database transaction.
		try {
			System.out.println(String.format("(ThreadId:JobId:JobType:DelayTime)-->(%s:%s:%s:%s)-->ACTION : Email Sending started...", Thread.currentThread().getId(), getJobId(), getJobType(), getDelayTime()));
			Thread.sleep(1000); //To simulate actual execution time
			System.out.println(String.format("(ThreadId:JobId:JobType:DelayTime)-->(%s:%s:%s:%s)-->ACTION : Sent all emails...", Thread.currentThread().getId(), getJobId(), getJobType(), getDelayTime()));
		} catch (Exception e) {
			//Rollback database transaction if any.
			throw new SendEmailJobException(e);
		} 
	}

}