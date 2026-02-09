package config;

import services.TaskManagerService;

public class TaskManagerBench {

    private final TaskManagerService taskManagerService;

    public TaskManagerBench() {
        this.taskManagerService = new TaskManagerService();
    }

    public TaskManagerService getTaskManagerService() {
        return taskManagerService;
    }

}
