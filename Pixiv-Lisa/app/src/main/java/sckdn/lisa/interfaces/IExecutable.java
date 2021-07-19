package sckdn.lisa.interfaces;

public interface IExecutable {

    void onStart();

    void run(IEnd end);

    void onEnd();
}
