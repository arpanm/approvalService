import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApprovalRequestDetailComponent } from './approval-request-detail.component';

describe('Component Tests', () => {
  describe('ApprovalRequest Management Detail Component', () => {
    let comp: ApprovalRequestDetailComponent;
    let fixture: ComponentFixture<ApprovalRequestDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ApprovalRequestDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ approvalRequest: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ApprovalRequestDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApprovalRequestDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load approvalRequest on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.approvalRequest).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
