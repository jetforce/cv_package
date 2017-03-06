package cv_package.forms;

import org.opencv.core.Mat;

import java.util.List;

public class TextAnswer extends Answer {

    List<List<Mat>> answers;

    public TextAnswer(List<List<Mat>> answers){
        super(Answer.OPTICAL_ANSWER);
        this.answers = answers;

    }


    @Override
    public Object getAnswer() {
        return this.answers;
    }
}
