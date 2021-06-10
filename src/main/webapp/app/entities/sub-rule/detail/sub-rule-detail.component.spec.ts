import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SubRuleDetailComponent } from './sub-rule-detail.component';

describe('Component Tests', () => {
  describe('SubRule Management Detail Component', () => {
    let comp: SubRuleDetailComponent;
    let fixture: ComponentFixture<SubRuleDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SubRuleDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ subRule: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SubRuleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SubRuleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load subRule on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.subRule).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
