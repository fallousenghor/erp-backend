import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MediasGalleryComponent } from './medias-gallery.component';

describe('MediasGalleryComponent', () => {
  let component: MediasGalleryComponent;
  let fixture: ComponentFixture<MediasGalleryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MediasGalleryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MediasGalleryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
