import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SubRuleInListItemDetailComponent } from './sub-rule-in-list-item-detail.component';

describe('Component Tests', () => {
  describe('SubRuleInListItem Management Detail Component', () => {
    let comp: SubRuleInListItemDetailComponent;
    let fixture: ComponentFixture<SubRuleInListItemDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SubRuleInListItemDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ subRuleInListItem: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SubRuleInListItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SubRuleInListItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load subRuleInListItem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.subRuleInListItem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
