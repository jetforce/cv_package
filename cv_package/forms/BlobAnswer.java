package cv_package.forms;

import org.opencv.core.Mat;

import java.util.List;

public class BlobAnswer extends Answer {

    Mat answers;

    public BlobAnswer(Mat answers){
        super(Answer.MARK_ANSWER);
        this.answers = answers;

    }


    @Override
    public Object getAnswer() {
        return this.answers;
    }
}
