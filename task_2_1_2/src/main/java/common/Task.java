package common;

import java.io.Serializable;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    public final int id;
    public final int[] chunk;

    public Task(int id, int[] chunk) {
        this.id = id;
        this.chunk = chunk;
    }
}
