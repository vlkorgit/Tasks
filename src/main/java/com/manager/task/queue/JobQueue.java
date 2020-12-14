package com.manager.task.queue;

import lombok.SneakyThrows;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;


public class JobQueue {

	//вопрос состоит в том, в какой момент нужно удалять эти записи
	//удалять при отправке клиенту в случае положительного результата? или может стоит хранить некоторое время? или
	// копировать их в бд
	private static int mapKey = 0;
	private Map<Integer, Future> tasks = new HashMap<>();

	ExecutorService executorService = Executors.newFixedThreadPool(4);

	public int addJob(Callable job) {
		mapKey++;
		tasks.put(mapKey, executorService.submit(job));
		return mapKey;
	}

	public JobStatus getJobStatus(int token) {
		Future job = tasks.get(token);
		if (job==null) return null;
		return getJobsStatusFor(job);
	}

	public Map<Integer, JobStatus> getJobs() {
		Map<Integer, JobStatus> result = new HashMap<>();
		tasks.forEach((key, job) -> {
			result.put(key, getJobsStatusFor(job));
		});
		return result;
	}

	public void clearJob(int token) {
		Future job = tasks.get(token);
		if (job == null) return;
		if (!job.isDone()) job.cancel(true);
		tasks.remove(token);
	}

	public void clearJobs() {
		tasks.forEach((token, job) -> job.cancel(true));
		tasks.clear();
	}

	private JobStatus getJobsStatusFor(Future job){
		if (job.isDone()) {
			try {
				job.get(); //this can throw
				return JobStatus.DONE;
			} catch (Exception ex) {
				return JobStatus.FAILED;
			}
		}
		return JobStatus.PROCESS;
	}
}