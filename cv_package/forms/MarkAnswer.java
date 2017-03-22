package cv_package.forms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jet on 04/03/2017.
 */

public class MarkAnswer extends Answer {

    int[] answers;
    List<Integer> isMarked;

    public MarkAnswer(int[] answers){
        super(Answer.MARK_ANSWER);
        this.answers = answers;
        isMarked = new ArrayList<>();
    }


    @Override
    public Object getAnswer() {
        return this.answers;
    }
    @Override
    public Object getResult() {
        return this.isMarked;
    }

    public void addMarked(int value) {
        isMarked.add(value);
    }
}
