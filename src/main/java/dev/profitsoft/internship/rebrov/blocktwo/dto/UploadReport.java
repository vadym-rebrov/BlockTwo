package dev.profitsoft.internship.rebrov.blocktwo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadReport{
    private Integer success = 0;
    private Integer failed = 0;

    public static UploadReport sum(UploadReport a, UploadReport b){
        UploadReport result = new UploadReport();
        result.setSuccess(a.getSuccess() + b.getSuccess());
        result.setFailed(a.getFailed() + b.getFailed());
        return result;
    }
}
