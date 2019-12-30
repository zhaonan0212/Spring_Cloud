package com.delta.pattern;

import java.util.HashMap;
/*
   用一个已经建造好的实例作为模型,通过复制模型对象来创建一个和原型相似的新对象,
 */
public class ProtoTypeManager {

    private HashMap<String, Shape> hashMap = new HashMap<String,Shape>();

    public ProtoTypeManager(){
        hashMap.put("Circle",new Circle());
        hashMap.put("Square",new Square());
    }

    public void addshape(String key,Shape shape){
        hashMap.put(key,shape);
    }

    public Shape getShape(String key){
        Shape temp = hashMap.get(key);
        return (Shape) temp.clone();
    }
}
