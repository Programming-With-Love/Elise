package site.zido.elise.client.scheduler;

import site.zido.elise.Operator;
import site.zido.elise.scheduler.AbstractScheduler;
import site.zido.elise.scheduler.DefaultOperator;
import site.zido.elise.scheduler.Seed;
import site.zido.elise.scheduler.SpiderContext;
import site.zido.elise.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a spider context in memory
 * @author zido
 */
public class MemorySpiderContext implements SpiderContext {
    private Map<Task, ContextItem> itemMap = new HashMap<>();

    @Override
    public Operator init(Task task, AbstractScheduler scheduler) {
        ContextItem item = new ContextItem();
        item.state = SpiderContext.STATE_START;
        item.seeds = new ArrayList<>(0);
        item.operator = new DefaultOperator(task, scheduler);
        itemMap.put(task, item);
        return item.operator;
    }

    @Override
    public byte getState(Task task) {
        return itemMap.get(task).state;
    }

    @Override
    public Operator getOperator(Task task) {
        return itemMap.get(task).operator;
    }

    @Override
    public List<Seed> getSeeds(Task task) {
        return itemMap.get(task).seeds;
    }

    @Override
    public void release(Task task) {
        itemMap.remove(task);
    }

    @Override
    public void setState(Task task, byte newState) {
        itemMap.get(task).state = newState;
    }

    @Override
    public void setState(byte oldState, byte newState) {
        itemMap.forEach((task, contextItem) -> {
            if (contextItem.state == STATE_START) {
                contextItem.state = STATE_PAUSE;
            }
        });
    }

    @Override
    public List<Task> getTasksByState(byte state) {
        List<Task> tasks = new ArrayList<>();
        for (Map.Entry<Task, ContextItem> entry : itemMap.entrySet()) {
            if (entry.getValue().state == state) {
                tasks.add(entry.getKey());
            }
        }
        return tasks;
    }


    private class ContextItem {
        volatile byte state;
        List<Seed> seeds;
        Operator operator;
    }
}
