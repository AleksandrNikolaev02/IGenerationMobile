package com.example.igenerationmobile.glide;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.InputStream;

public class SvgDecoder implements ResourceDecoder<InputStream, SVG> {
    @Override
    public boolean handles(@NonNull InputStream source, @NonNull Options options) {
        // Проверка, является ли источник SVG
        return true;
    }

    @Override
    public Resource<SVG> decode(@NonNull InputStream source, int width, int height, @NonNull Options options) {
        try {
            // Парсинг SVG и создание Drawable
            SVG svg = SVG.getFromInputStream(source);
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            return new SimpleResource<>(svg);
        } catch (SVGParseException e) {
            // Обработка ошибки парсинга SVG
            throw new RuntimeException("Не удалось распарсить SVG", e);
        }
    }
}
