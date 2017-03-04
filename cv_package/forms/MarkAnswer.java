package cv_package.forms;

/**
 * Created by Jet on 04/03/2017.
 */

public class MarkAnswer extends Answer {

    int[] answers;

    public MarkAnswer(int[] answers){
        super(Answer.MARK_ANSWER);
        this.answers = answers;

    }


    @Override
    public Object getAnswer() {
        return this.answers;
    }
}
