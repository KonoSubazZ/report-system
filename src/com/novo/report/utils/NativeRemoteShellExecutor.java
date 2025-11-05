package com.novo.report.utils;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 远程执行 Shell 脚本/命令的工具类
 * 依赖 JSch 库（SSH2 客户端实现）
 */
public class NativeRemoteShellExecutor {
    // 远程服务器 IP
    private String host;
    // 远程服务器端口（默认 22）
    private int port = 22;
    // 登录用户名
    private String username;
    // 登录密码（或密钥）
    private String password;
    // SSH 会话
    private Session session;

    /**
     * 构造方法（使用密码认证）
     * @param host 远程服务器 IP
     * @param username 登录用户名
     * @param password 登录密码
     */
    public NativeRemoteShellExecutor(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    /**
     * 构造方法（指定端口 + 密码认证）
     * @param host 远程服务器 IP
     * @param port 端口号（如 2222）
     * @param username 登录用户名
     * @param password 登录密码
     */
    public NativeRemoteShellExecutor(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * 连接远程服务器
     * @return 连接是否成功
     */
    public boolean connect() throws JSchException {
        JSch jsch = new JSch();
        // 创建会话
        session = jsch.getSession(username, host, port);
        session.setPassword(password);

        // 配置 SSH 连接（跳过主机密钥检查，生产环境可根据需求修改）
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no"); // 不检查主机密钥
        session.setConfig(config);

        // 连接超时时间（30秒）
        session.connect(30000);
        return session.isConnected();
    }

    /**
     * 执行单个 Shell 命令
     * @param command 命令（如 "ls -l /home"）
     * @return 命令输出结果（每行一个元素）
     */
    public List<String> executeCommand(String command) throws JSchException, IOException {
        if (!session.isConnected()) {
            throw new IllegalStateException("未连接到远程服务器，请先调用 connect()");
        }

        Channel channel = null;
        List<String> result = new ArrayList<>();
        try {
            // 打开执行命令的通道
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // 获取命令输出流
            InputStream in = channel.getInputStream();
            // 获取错误流（如命令执行失败的错误信息）
            InputStream err = ((ChannelExec) channel).getErrStream();

            // 启动通道
            channel.connect();

            // 读取输出结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }

            // 检查错误信息（如果有错误，添加到结果中）
            BufferedReader errReader = new BufferedReader(new InputStreamReader(err, "UTF-8"));
            while ((line = errReader.readLine()) != null) {
                result.add("[ERROR] " + line);
            }

        } finally {
            // 关闭通道
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
        }
        return result;
    }

    /**
     * 执行远程 Shell 脚本
     * @param scriptPath 脚本绝对路径（如 "/home/user/script.sh"）
     * @param args 脚本参数（可选，如 "param1 param2"）
     * @return 脚本输出结果
     */
    public List<String> executeScript(String scriptPath, String... args) throws JSchException, IOException {
        // 拼接脚本命令（如 "/home/user/script.sh param1 param2"）
        StringBuilder command = new StringBuilder(scriptPath);
        for (String arg : args) {
            command.append(" ").append(arg);
        }
        return executeCommand(command.toString());
    }

    /**
     * 断开与远程服务器的连接
     */
    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    /**
     * 测试示例
     */
    public static void main(String[] args) {
        // 远程服务器信息（替换为实际配置）
        String host = "192.168.1.100";
        int port = 22;
        String username = "root";
        String password = "your_password";

        NativeRemoteShellExecutor executor = new NativeRemoteShellExecutor(host, port, username, password);
        try {
            // 连接服务器
            if (executor.connect()) {
                System.out.println("连接远程服务器成功");

                // 示例1：执行单个命令
                System.out.println("\n=== 执行命令: ls -l /tmp ===");
                List<String> cmdResult = executor.executeCommand("ls -l /tmp");
                cmdResult.forEach(System.out::println);

                // 示例2：执行 Shell 脚本（带参数）
                System.out.println("\n=== 执行脚本: /home/user/test.sh hello world ===");
                List<String> scriptResult = executor.executeScript("/home/user/test.sh", "hello", "world");
                scriptResult.forEach(System.out::println);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 断开连接
            executor.disconnect();
            System.out.println("\n已断开远程连接");
        }
    }
}