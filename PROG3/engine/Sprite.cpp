#include "Sprite.h"
#include "InputHandler.h"
#include <iostream>
#include <cmath>


namespace engine {

Sprite::Sprite(std::string path, GameEngine* win, int x, int y, int g , int sheetWidth, int sheetHeight): //Ska flip/angle initieras h�r eller vid deklarationen?
	                GameObject(path, win, x, y), sheetWidth(sheetWidth), sheetHeight(sheetHeight), angle(0), flip(SDL_FLIP_NONE), GRAVITY(g) {
    srcRect.w /= sheetWidth;
    srcRect.h /= sheetHeight;
    destRect.w /= sheetWidth;
    destRect.h /= sheetHeight;
}

std::shared_ptr<Sprite> Sprite::getInstance(std::string path, GameEngine* win, int x, int y, int g, int sheetWidth, int sheetHeight) {
    return std::shared_ptr<Sprite>(new Sprite(path, win, x, y, g, sheetWidth, sheetHeight));
}

void Sprite::tick() {
    move();
}

void Sprite::draw() {
    for (auto com : commands) 
        if (InputHandler::getInstance().isKeyDown(com.first))
            com.second();
    SDL_RenderCopyEx(window->getRenderer(), tx, &srcRect, &destRect, angle, nullptr, flip);
}

void Sprite::setFlip(FLIP f) {
    if (f == HORIZONTAL_FLIP)
        flip = SDL_FLIP_HORIZONTAL;
    else if (f == SDL_FLIP_VERTICAL)
        flip = SDL_FLIP_VERTICAL;
    else if (f == SDL_FLIP_NONE)
        flip = SDL_FLIP_NONE;
}

void Sprite::setAngle(double ang) {
    angle = ang;
}

double Sprite::getAngle() const {
    return angle;
}

void Sprite::addCommand(SDL_Keycode key, std::function<void()> func) {
    commands[key] = func;
}

void Sprite::nextFrame(int row) {
    if (row < 1 || row > sheetHeight)
        throw std::logic_error("Row doesn't match the given height of the sprite sheet");
    currentFrame = int(((SDL_GetTicks() / 100) % sheetWidth));
    srcRect.y = srcRect.h * (row - 1);
    srcRect.x = srcRect.w * currentFrame;
}

void Sprite::adjustAcceleration(double x, double y) {
    acceleration.x += x;
    acceleration.y += y;
}

double Sprite::getAccelerationX() const {
    return acceleration.x;
}
double Sprite::getAccelerationY() const {
    return acceleration.x;
}

bool Sprite::isGrounded() const {
    return isOnGround;
}

void Sprite::setGrounded(bool b) {
    isOnGround = b;
}

void Sprite::move() {
    double xAccel = acceleration.x;
    double yAccel = acceleration.y;
    const double DRAG = 0.9;
    const double MAX = 500;

    double xVel = 0;
    double yVel = 0;

    if (abs(xVel) < MAX)
        xVel += xAccel;
    if (abs(xAccel) == 0)
        xVel *= DRAG;

    if (!isOnGround)
        yVel += GRAVITY + yAccel;
    yVel += yAccel;

    this->adjustAcceleration(-xAccel*DRAG*0.4,-yAccel*DRAG*0.4);
    if (abs(xAccel) < 0.15)
        adjustAcceleration(-acceleration.x,0);
    if (abs(yAccel) < 0.15)
        adjustAcceleration(0,-acceleration.y);
 
    destRect.x += xVel;
    destRect.y += yVel;
}

void Sprite::bounce(int bounciness, int collisionSpeedX, int collisionSpeedY) {
    acceleration.x = collisionSpeedX+-acceleration.x*bounciness;
    if(!isOnGround)
        acceleration.y = collisionSpeedY+ -GRAVITY*(bounciness*0.4);
}

Sprite::~Sprite() {
}

} /* namespace engine */
