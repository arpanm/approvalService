import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApprovalRequestItemDetailComponent } from './approval-request-item-detail.component';

describe('Component Tests', () => {
  describe('ApprovalRequestItem Management Detail Component', () => {
    let comp: ApprovalRequestItemDetailComponent;
    let fixture: ComponentFixture<ApprovalRequestItemDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ApprovalRequestItemDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ approvalRequestItem: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ApprovalRequestItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApprovalRequestItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load approvalRequestItem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.approvalRequestItem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
