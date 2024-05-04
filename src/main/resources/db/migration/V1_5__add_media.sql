CREATE TABLE MediaEntity (
    id UUID PRIMARY KEY UNIQUE
);

CREATE TABLE ProductVariantVersionToMediaEntity (
    productVariantVersionId UUID,
    mediaId UUID,
    FOREIGN KEY (productVariantVersionId) REFERENCES ProductVariantVersionEntity(id),
    FOREIGN KEY (mediaId) REFERENCES MediaEntity(id)
);