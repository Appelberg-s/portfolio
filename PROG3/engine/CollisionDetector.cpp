#include "CollisionDetector.h"
#include "GameObject.h"

namespace engine {
std::shared_ptr<GameObject> CollisionDetector::collisionRect(std::shared_ptr<GameObject> obj1, std::shared_ptr<GameObject> obj2) {
    SDL_Rect temp = rectCheck(obj1, obj2);
    if (temp.w > 0 && temp.h > 0 && obj1 != obj2)
        return obj2;
    return nullptr;
}

std::shared_ptr<GameObject> CollisionDetector::collisionPixel(std::shared_ptr<GameObject> obj1, std::shared_ptr<GameObject> obj2) {
    SDL_Rect temp = rectCheck(obj1, obj2);
    if (temp.w > 0 && temp.h > 0) {
        SDL_Rect area1 = getSearchArea(temp, obj1);
        SDL_Rect area2 = getSearchArea(temp, obj2);

        for(int y = 0; y < temp.h; y++)
            for(int x = 0; x < temp.w; x++)
                if(obj1 != obj2 && checkPixel(*obj1->getSurface(), area1.x + x, area1.y + y) && checkPixel(*obj2->getSurface(), area2.x + x, area2.y + y))
                    return obj2;
    }
    return nullptr;
}

std::shared_ptr<GameObject> CollisionDetector::collisionRect(std::shared_ptr<GameObject> obj1, std::vector<std::shared_ptr<GameObject>> vec) {
    for (std::shared_ptr<GameObject> g: vec)
        if (collisionRect(obj1, g) != nullptr) 
            return g;
    return nullptr;
}

std::shared_ptr<GameObject> CollisionDetector::collisionPixel(std::shared_ptr<GameObject> obj1, std::vector<std::shared_ptr<GameObject>> vec) {
    for (std::shared_ptr<GameObject> g: vec)
        if (collisionPixel(obj1, g) != nullptr)
            return g;
    return nullptr;
}

SDL_Rect CollisionDetector::rectCheck(std::shared_ptr<GameObject> obj1, std::shared_ptr<GameObject> obj2){
    SDL_Rect result = {0,0,0,0};
    SDL_IntersectRect(&obj1->getDestRect(),&obj2->getDestRect(), &result);
    return result;
}

bool CollisionDetector::checkPixel(SDL_Surface& surf, int x, int y) {
    int bpp = surf.format->BytesPerPixel;
    Uint8* p = (Uint8*) surf.pixels + y * surf.pitch + x * bpp;
    Uint32 pixelColor;

    switch(bpp) {
    case(1):
        pixelColor = *p;
    break;
    case(2):
        pixelColor = *(Uint16*)p;
    break;
    case(3):
        if(SDL_BYTEORDER == SDL_BIG_ENDIAN)
            pixelColor = p[0] << 16 | p[1] << 8 | p[2];
        else
            pixelColor = p[0] | p[1] << 8 | p[2] << 16;
    break;
    case(4):
        pixelColor = *(Uint32*)p;
    break;
    }

    Uint8 red, green, blue, alpha;
    SDL_GetRGBA(pixelColor, surf.format, &red, &green, &blue, &alpha);
    return alpha > 200;
}

SDL_Rect CollisionDetector::getSearchArea(SDL_Rect& intersect, std::shared_ptr<GameObject> obj) {
    SDL_Rect searchArea =
    {intersect.x-obj->getDestRect().x+obj->getSrcRect().x,
            intersect.y-obj->getDestRect().y+obj->getSrcRect().y,
            intersect.w,
            intersect.h};

    return searchArea;
}

} /* namespace engine */
