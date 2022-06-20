package ru.utils.interfaces;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public interface IProcessable {

    void execute(BufferedReader reader, BufferedWriter writer) throws IOException;
}
