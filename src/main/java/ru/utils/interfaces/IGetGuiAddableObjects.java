package ru.utils.interfaces;

import ru.gui.elements.GuiCloneableObject;
import ru.gui.scenes.main.tabs.resumes.elements.GuiAddableObject;

import java.util.List;

public interface IGetGuiAddableObjects<T extends GuiCloneableObject<T>> {
    List<GuiAddableObject<T>> getObjects();
}
