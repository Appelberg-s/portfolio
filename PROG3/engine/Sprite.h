#ifndef SPRITE_H_
#define SPRITE_H_

#include "GameObject.h"
#include "Acceleration.h"

#include <functional>
#include <map>
#include <memory>

namespace engine {

enum FLIP {
	HORIZONTAL_FLIP = SDL_FLIP_HORIZONTAL,
	VERTICAL_FLIP = SDL_FLIP_VERTICAL,
	NO_FLIP = SDL_FLIP_NONE
};

class Sprite: public GameObject {
public:
	static std::shared_ptr<Sprite> getInstance(std::string path, GameEngine* win, int x, int y, int g = 10, int sheetWidth = 1, int sheetHeight = 1);
	virtual void tick() override;
	virtual void draw() override;
	void setFlip(FLIP);
	void setAngle(double);
	double getAngle() const;
	void addCommand(SDL_Keycode, std::function<void(void)>);
	void nextFrame(int row = 1);
	bool isGrounded() const;
	void setGrounded(bool b);
    void adjustAcceleration(double x, double y);
    double getAccelerationX() const;
    double getAccelerationY() const;
    void move();
    void bounce(int bounciness, int collisionSpeedX, int collisionSpeedY);

	virtual ~Sprite();
	
protected:
	Sprite(std::string path, GameEngine* win, int x, int y, int g = 10, int sheetWidth = 1, int sheetHeight = 1);

private:
	int sheetWidth;
	int sheetHeight;
	int currentFrame = 1;
	double angle;
	SDL_RendererFlip flip;
	std::map<SDL_Keycode, std::function<void(void)>> commands;
	const int GRAVITY;
    Acceleration acceleration;
    bool isOnGround = false;
};

} /* namespace engine */

#endif /* SPRITE_H_ */
