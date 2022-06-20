package ru.gui.elements;

import ru.controllers.YListController;

/**
 * GuiYMoveableObject представляет собой клонируемый, перемещаемый и удаляемый объект-контейнер для списка перемещаемых объектов.
 *
 * Поскольку объект является контейнером для объектов типа T, наследуемых от объекта GuiCloneableObject<T>, то он может
 * содержать в себе другой GUI объект, а также отдавать его по запросу от других объектов.
 *
 * Использование Generic (Дженериков, то есть <T>) позволяет не писать один и тот же код для разных GUI объектов, а помещать
 * эти GUI объекты в единый контейнер.
 *
 * @param <T> тип GUI объекта, помещаемого в контейнер GuiYMoveableObject
 */
public class GuiYMoveableObject<T extends GuiCloneableObject<T>> extends GuiYListedObject {

    //GUI объект типа T
    private final T guiObject;
    //Список (контроллер) объектов типа GuiYMoveableObject<T>
    private final YListController<GuiYMoveableObject<T>> controller;
    //GUI объект с кнопками для управления объектами-контейнерами
    private final GuiYControlButtons buttons;

    public GuiYMoveableObject(T guiObject, YListController<GuiYMoveableObject<T>> controller, int index,
                              double x, double y, double width, double height, double gap,
                              double buttonsX, double buttonsY, double buttonsWidth, double buttonsGap,
                              boolean isHorizontal, boolean hasAddButton) {
        super(index, x, y, width, height, gap);
        this.guiObject = guiObject;
        this.controller = controller;
        buttons = new GuiYControlButtons(buttonsX, buttonsY, buttonsWidth, buttonsGap, isHorizontal, hasAddButton, this);
        root.getChildren().addAll(
                guiObject.getRoot(),
                buttons.getRoot()
        );
    }

    /**
     * Метод получения GUI объекта типа T, внутри которого содержатся все элементы GUI
     * @return объект типа T
     */
    public T getGuiObject() {
        return guiObject;
    }

    /**
     * Метод добавления чистой копии объекта в список после этого объекта
     */
    public void addAfter() {
        controller.addAfter(
                new GuiYMoveableObject<>(
                        guiObject.getClone(),
                        controller,
                        index, x, y, width, height, gap,
                        buttons.x, buttons.y, buttons.getButtonsWidth(),
                        buttons.getButtonsGap(), buttons.isHorizontal(),
                        buttons.isHasAddButton())
        );
    }

    /**
     * Метод перемещения объекта вверх в списке
     */
    public void moveUp() {
        controller.moveUp(index);
    }

    /**
     * Метод перемещения объекта вниз в списке
     */
    public void moveDown() {
        controller.moveDown(index);
    }

    /**
     * Метод удаления объекта из списка
     */
    public void remove() {
        controller.remove(index);
    }

    /**
     * Метод очистки объекта от данных
     */
    @Override
    public void clear() {
        guiObject.clear();
    }
}
