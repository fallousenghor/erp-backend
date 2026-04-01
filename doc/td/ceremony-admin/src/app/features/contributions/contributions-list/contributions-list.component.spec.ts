import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContributionsListComponent } from './contributions-list.component';

describe('ContributionsListComponent', () => {
  let component: ContributionsListComponent;
  let fixture: ComponentFixture<ContributionsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContributionsListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContributionsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
