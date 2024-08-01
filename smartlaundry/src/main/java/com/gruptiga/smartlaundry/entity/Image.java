package com.gruptiga.smartlaundry.entity;

import com.gruptiga.smartlaundry.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.IMAGE)
public class Image {
    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.gruptiga.smartlaundry.utils.ImageCustomId")
    @Column(name = "image_id")
    private String imageId;

    @Column(name = "image_name", nullable = false)
    private String imageName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "path", nullable = false)
    private String path;
}
