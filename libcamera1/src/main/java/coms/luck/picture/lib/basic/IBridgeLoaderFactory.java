package coms.luck.picture.lib.basic;

import coms.luck.picture.lib.loader.IBridgeMediaLoader;

/**
 * @author：luck
 * @date：2022/6/10 9:37 上午
 * @describe：IBridgeLoaderFactory
 */
public interface IBridgeLoaderFactory {
    /**
     * CreateLoader
     */
    IBridgeMediaLoader onCreateLoader();
}
