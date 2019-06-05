# springboot+ajax登录注册的问题小结

## 1.@ResponseBody标注

该注释表示该方法返回的数据是json数据，如果忘记加该注释，那么前台就直接进入error方法了

## 2.ajax中dataType

dataType：“text”替代"json"

