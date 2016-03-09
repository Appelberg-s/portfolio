#ifndef COLLISIONDETECTOR_H_
#define COLLISIONDETECTOR_H_

#include <vector>
#include <memory>

#include <SDL.h>

namespace engine {
class GameObject;
class CollisionDetector {
public:
	static std::shared_ptr<GameObject> collisionRect(std::shared_ptr<GameObject> obj1, std::shared_ptr<GameObject> obj2);
	static std::shared_ptr<GameObject> collisionPixel(std::shared_ptr<GameObject> obj1, std::shared_ptr<GameObject> obj2);
	static std::shared_ptr<GameObject> collisionRect(std::shared_ptr<GameObject> obj1, std::vector<std::shared_ptr<GameObject>> vec);
	static std::shared_ptr<GameObject> collisionPixel(std::shared_ptr<GameObject> obj1, std::vector<std::shared_ptr<GameObject>> vec);
private:
	CollisionDetector() = delete;
    ~CollisionDetector() = delete;
	static SDL_Rect rectCheck(std::shared_ptr<GameObject> obj1, std::shared_ptr<GameObject> obj2);
	static bool checkPixel(SDL_Surface& surface, int x, int y);
	static SDL_Rect getSearchArea(SDL_Rect& intersect, std::shared_ptr<GameObject> obj);

	CollisionDetector(const CollisionDetector&) = delete;
    const CollisionDetector& operator=(const CollisionDetector&) = delete;
};

} /* namespace engine */

#endif /* COLLISIONDETECTOR_H_ */
