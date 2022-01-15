# Jupyer安装Java内核

## 安装JDK11以上

设置好环境变量

## 安装Anaconda3

安装好 Jupyer Notebook、Jupyer Lab（可选）

## 安装ijava

- 管理员权限打开Anancoda Prompt命令行，确保IJava没有安装过，执行jupyter kernelspec remove java

- 接着下载IJava的安装程序，网址：https://github.com/SpencerPark/IJava/releases/tag/v1.3.0

- 解压到合适的目录（没有中文、没有特殊符号、没有空格的目录）

- Anancoda Prompt命令行中cmd到解压的目录，执行命令安装java内核：

  ```bash
  python install.py --sys-prefix
  ```

- 输入以下命令查看是否安装成功（是否出现Java）

  ```bash
  jupyter kernelspec list
  ```

- 安装成功后，若前面没有切换环境变量则需要找到anacoda的安装目录找到\>share>jupyter>kernel>java：

- 打开kernel.json文件，将第二行的java的前边添加jdk bin的路径，文件间用//.

  ```bash
  C:\\Program Files\\Java\\jdk-17\\bin\\java.exe
  ```

## Jupyter 安装代码补全插件

先关闭jupyter notebook

使用豆瓣镜像安装插件，管理员打开Anancoda Prompt命令行，执行命令安装：

```bash
pip install jupyter_contrib_nbextensions -i https://pypi.douban.com/simple
pip install jupyter_nbextensions_configurator -i https://pypi.douban.com/simple
```

配置 nbextension,Anancoda Prompt命令行执行：

```bash
jupyter contrib nbextension install --user --skip-running-check
```

在Jupyter Notebook里开启功能

如果上面两个步骤都没报错，启动Jupyter Notebook，上面选项栏会出现 Nbextensions 的选项：

勾选Hinterland即可。

## 使用

http://localhost:8888/ 

新建java notebook

输入，执行看看有没有输出

```java
System.out.println("hello world");
```

