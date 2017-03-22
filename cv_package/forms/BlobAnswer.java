package cv_package.forms;

import org.opencv.core.Mat;

import java.util.List;

public class BlobAnswer extends Answer {

    Mat answers;
    String image_path;

    public BlobAnswer(Mat answers){
        super(Answer.MARK_ANSWER);
        this.answers = answers;

    }


    @Override
    public Object getAnswer() {
        return this.answers;
    }
    @Override
    public Object getResult() {
        return this.image_path;
    }

    public void setPath(String path) {
        image_path = path;
    }
}
