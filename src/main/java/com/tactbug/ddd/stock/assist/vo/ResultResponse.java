package com.tactbug.ddd.stock.assist.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class ResultResponse implements BaseVo{
    private String code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    public ResultResponse(BaseVo BaseVo){
        this.code = BaseVo.code();
        this.message = BaseVo.message();
    }

    public static ResultResponse ok(){
        return new ResultResponse(BaseResultEnum.OK);
    }

    public static ResultResponse error(){
        return new ResultResponse(BaseResultEnum.SYSTEM_ERROR);
    }

    public static ResultResponse error(BaseVo baseVo){
        return new ResultResponse(baseVo);
    }

    public ResultResponse data(String key, Object value){
        data.put(key, value);
        return this;
    }

    public ResultResponse data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}
