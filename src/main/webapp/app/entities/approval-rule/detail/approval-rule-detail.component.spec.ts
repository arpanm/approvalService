import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApprovalRuleDetailComponent } from './approval-rule-detail.component';

describe('Component Tests', () => {
  describe('ApprovalRule Management Detail Component', () => {
    let comp: ApprovalRuleDetailComponent;
    let fixture: ComponentFixture<ApprovalRuleDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ApprovalRuleDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ approvalRule: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ApprovalRuleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApprovalRuleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load approvalRule on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.approvalRule).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
