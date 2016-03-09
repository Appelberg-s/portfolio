#ifndef TEXTLABEL_H_
#define TEXTLABEL_H_

#include "MenuObject.h"

namespace engine {

class TextLabel: public MenuObject {
public:
	static std::shared_ptr<TextLabel> getInstance(GameEngine* win, int x, int y, std::string text);
	void setFontSize(int size);
	void setColor(Uint8 r, Uint8 g, Uint8 b, Uint8 a = 0xFF);
	void draw() override;
	std::string getText() const;

	~TextLabel();
protected:
	TextLabel(GameEngine* win, int x, int y, std::string text);
	SDL_Texture* textTexture = nullptr;
	SDL_Color _color = { 0x00, 0x00, 0x00, 0xFF };
	int fontSize = 12;
	std::string text;
	void updateTexture();
};

} /* namespace engine */

#endif /* TEXTLABEL_H_ */
