#ifndef TTFMANAGER_H_
#define TTFMANAGER_H_

#include <SDL_ttf.h>
#include <string>

namespace engine {
	
class TTFManager {
public:
	static TTFManager& getInstance();

	void setFont(std::string fontname);
	void setFontSize(int size);
	TTF_Font* getFont() const;
	void reset();
	void resizeRect(std::string text, SDL_Rect& destRect) const;
	SDL_Texture* createFontTexture(SDL_Renderer* ren, std::string text, int size, SDL_Color color);

	~TTFManager();
	TTFManager(const TTFManager&) = delete;
	TTFManager& operator=(const TTFManager&) = delete;
private:
	const std::string FONT_PATH = "fonts/";
	const std::string DEFAULT_FONT = "arial.ttf";
	const int DEFAULT_SIZE = 12;
	SDL_Color DEFAULT_COLOR = { 0, 0, 0, 0xFF };
	std::string _fontName = "arial.ttf";
	int _size = 12;
	TTF_Font* currentFont;
	void openFont(std::string);
	TTFManager();
};

}
#endif


